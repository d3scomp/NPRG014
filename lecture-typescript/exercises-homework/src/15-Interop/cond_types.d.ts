// TypeScript definitions for JavaScript code in config.js

export interface Config {
    username: string;
    apiEndpoint: string;
    password: string;
    apiKey: string;
}

// Conditionally include sensitive info based on a boolean flag
export type ConfigInfo<T extends boolean> = T extends true
    ? Config
    : Omit<Config, 'password' | 'apiKey'>;

/**
 * Retrieves a configuration object.
 *
 * @param includeSensitive - A boolean indicating whether to include sensitive information.
 * @returns The configuration object with or without sensitive information based on the parameter.
 */
export declare function getConfig<T extends boolean>(includeSensitive: T): ConfigInfo<T>;
