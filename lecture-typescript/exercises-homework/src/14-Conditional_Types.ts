namespace e14 {
    type Config = {
        username: string;
        apiEndpoint: string;
        password: string;
        apiKey: string;
    }

    // Conditionally include sensitive info based on a boolean flag
    type ConfigInfo<T extends boolean> = T extends true
        ? Config
        : Omit<Config, 'password' | 'apiKey'>;

    // Omit is a library type, which omits fields from a type
    // There are a number of other library types for type manipulation
    // See https://www.typescriptlang.org/docs/handbook/utility-types.html

    const config: Config = {
        username: "user1",
        apiEndpoint: "https://api.example.com",
        password: "supersecret",
        apiKey: "12345-ABCDE"
    };

    function getConfig<T extends boolean>(includeSensitive: T): ConfigInfo<T> {
        if (includeSensitive) {
            return config as ConfigInfo<T>;
        } else {
            const {password, apiKey, ...nonSensitiveConfig} = config;
            return nonSensitiveConfig as ConfigInfo<T>;
        }
    }

    // Usage examples:
    const fullConfig = getConfig(true);  // Includes all info
    const safeConfig = getConfig(false); // Excludes sensitive info

    // If the type cannot be determined, the result is a union of all potential
    const randomBool = Math.random() > 0.5;
    const undeterminedConfig = getConfig(randomBool);

    console.log(fullConfig); // Outputs: Object with all properties
    console.log(safeConfig); // Outputs: Object without 'password' and 'apiKey'


    // -----------------------------------------
    // Distributive Conditional Types -- When applied on a union, the result is again a union
    type ConfigWithSecret = {
        username: string;
        apiSecret: string;
    }

    type ConfigWithPassword = {
        username: string;
        password: string;
    }

    type HashedConfig<U> = U extends { password: string } ? { hashedPassword: string } & Omit<U, 'password'> :
        U extends { apiKey: string } ? { hashedApiKey: string } & Omit<U, 'apiKey'> :
            never;

    function hashSecrets<U>(configs: U[]): HashedConfig<U>[] {
        throw new Error("Not implemented");
    }

    // Inferred type is:
    // (
    //     ({ hashedApiKey: string } & Omit<{
    //         username: string
    //         apiKey: string
    //         password?: undefined
    //     }, "apiKey">)
    //     |
    //     ({ hashedPassword: string } & Omit<{
    //         username: string
    //         password: string
    //         apiKey?: undefined
    //     }, "password">)
    // )[]
    const hs = hashSecrets([
        {
            username: "user1",
            apiKey: "12345-ABCDE"
        },
        {
            username: "user1",
            password: "supersecret",
        }
    ]);
}