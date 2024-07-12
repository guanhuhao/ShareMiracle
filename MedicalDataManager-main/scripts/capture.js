var page = require('webpage').create();
var system = require('system');

const args = {};
const argsStack = [];
system.args.forEach(function(piece) {
    if (piece.slice(0, 2) == '--') {
        const argName = piece.slice(2);
        argsStack.push(argName);
    } else if (argsStack.length > 0) {
        const argName = argsStack.pop();
        args[argName] = piece;
    }
});
const url = args['url'];
const domain = url.split('://')[1].split('.')[0];

page.viewportSize = { width: 1920, height: 1080 };
page.open(url, function() {
    const title = page.evaluate(function() {
        return document.title;
    })
    console.log(title);
    phantom.exit();
});