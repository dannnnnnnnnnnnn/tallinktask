const webdriver = require('selenium-webdriver');

const driver = new webdriver.Builder()
    .forBrowser('chrome')
    .build();

// ask the browser to open a page
driver.navigate().to('localhost:3000/');