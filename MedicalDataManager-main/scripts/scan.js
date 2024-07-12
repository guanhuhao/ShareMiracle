var page = require('webpage').create();
var system = require('system');
var fs = require('fs');

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

page.open(url, function() {
    var arrays = page.evaluate(function() {
        var doms = document.getElementsByTagName('*');
        var arrays = [];
        var i, tagName, className, innerText;
        for (i in doms) {
            tagName = doms[i].tagName;
            className = doms[i].className;
            innerText = doms[i].innerText;
            if (innerText && innerText.length > 0 && tagName !== 'SCRIPT' && tagName !== 'STYLE') {
                arrays.push({
                    tagName: doms[i].tagName,
                    className: doms[i].className,
                    innerText: doms[i].innerText
                });
            }
        }

        return arrays;
    });

    var arrayString = JSON.stringify(arrays, null, '  ');
    fs.write('text.json', arrayString, 'w');
    
    phantom.exit();
});