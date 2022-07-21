const path = require("path");
config.browserConsoleLogOptions.level = "debug";

const basePath = config.basePath;
const projectPath = path.resolve(basePath, "..", "..", "..");
const wasmPath = path.resolve(projectPath, "node_modules", "@matrix-org", "olm")

const debug = message => console.log(`[karma-config] ${message}`);
debug(`karma basePath: ${basePath}`);
debug(`karma wasmPath: ${wasmPath}`);

config.proxies = {
    "/olm.wasm": path.resolve(wasmPath, 'olm.wasm')
}

config.files = [
    path.resolve(wasmPath, "olm.js"),
    {pattern: path.resolve(wasmPath, "olm.wasm"), included: false, served: true, watched: false}
].concat(config.files);

config.mime = config.mime || {}
config.mime['application/wasm'] = ['wasm']