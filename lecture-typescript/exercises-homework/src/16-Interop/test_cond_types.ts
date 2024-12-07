import {getConfig} from "./cond_types";

const fullConfig = getConfig(true);  // Includes all info
const safeConfig = getConfig(false); // Excludes sensitive info

console.log(fullConfig.apiKey);
// console.log(safeConfig.apiKey); -- does not compile