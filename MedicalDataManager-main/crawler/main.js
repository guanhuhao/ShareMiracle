// const puppeteer = require('puppeteer');

// (async () => {
//     const browser = await puppeteer.launch();
//     const page = await browser.newPage();
//     page.setViewport({width: 1920, height: 1080});

//     const seconds = 2 * 60 + 3;
//     await page.goto('https://www.bilibili.com/video/BV1R1421i7dw/?share_source=copy_web&vd_source=5c7883370b8ac639e64c6b403ee2f3aa&t=' + seconds, { waitUntil: 'networkidle0' });

//     await page.screenshot({ path: 'bilibili.png' });
//     await browser.close();
// })();

const SerpApi = require('google-search-results-nodejs');
const search = new SerpApi.GoogleSearch("AIzaSyCuSQMMZQYxueoPqc9Vj1JPpksl2pHLvHc");


let result = search.json({
    q: "medical 4DCT datasets",
}, (data) => {
    console.log(data)
});

console.log(result);