const config = {
    username: "user1",
    apiEndpoint: "https://api.example.com",
    password: "supersecret",
    apiKey: "12345-ABCDE"
};

export function getConfig(includeSensitive) {
    if (includeSensitive) {
        return config;
    } else {
        const { password, apiKey, ...nonSensitiveConfig } = config;
        return nonSensitiveConfig;
    }
}

