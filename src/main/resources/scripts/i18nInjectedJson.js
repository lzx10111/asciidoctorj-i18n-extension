const i18nDefaultLang = "null";
const i18nPrefix = "null";
const i18nKeyPrefix = "null";

function loadTranslations(lang) {
    const json = document.getElementById('i18n-lang-json-' + lang).textContent;

    return JSON.parse(json);
}

function translatePage(lang) {
    const translations = loadTranslations(lang);

    document.documentElement.lang = lang;

    const elements = document.querySelectorAll('.' + i18nPrefix);
    let key = null;
    let value = null;

    for (const elem of elements) {
        key = getJsonKey(elem);

        try {
            if (!key) throw new Error('The key class was not specified or is in an incorrect format.');
        } catch (error) {
            console.error(error);
            break;
        }

        value = getValueByKey(translations, key);

        if (value) {
            translateElement(elem, value);
        }
        else {
            console.warn(`The following key '${key}' was not found in the json file for the lang '${lang}'.`);
        }
    }
}

function getJsonKey(elem) {
    const classesArray = [...elem.classList];
    const delimiter = "_";
    let index = null;
    let key = null;

    for (const c of classesArray) {
        if (c.includes(i18nKeyPrefix)) {
            index = c.indexOf(delimiter);
            key = c.slice(index + 1);
            key = key.replaceAll(delimiter, ".");
        }
    }

    return key;
}

function getValueByKey(obj, key) {
    return key.split('.').reduce(
        (acc, part) => acc && acc[part], obj);
}

function translateElement(elem, value) {

    if (elem.tagName === "TABLE") {
        const elements = elem.getElementsByTagName("tr");
        let index = 0;

        for (const e of elements) {
            translateTr(e, value[index], index);
            index++;
        }

        return;
    }

    if (elem.tagName === "SELECT") {
        const elements = elem.getElementsByTagName("option");
        let index = 0;
        const keys = Object.keys(value[index]);

        for (const e of elements) {
            translateElement(e, value[index][keys[0]]);
            index++;
        }

        return;
    }

    if (hasHTML(value)) {
        changeInnerHTML(elem, value);
    }
    else {
        changeTextContent(elem, value);
    }

    return;
}

function translateTr(elem, value, index) {
    let row = null;

    if (index === 0) {
        row = elem.getElementsByTagName("th");
    }
    else {
        row = elem.getElementsByTagName("td");
    }

    const keys = Object.keys(value);
    let indexCell = 0;

    for (const cell of row) {
        if (cell.childElementCount > 0) {
            const c = cell.querySelector("p");
            translateElement(c, value[keys[indexCell]]);
        }
        else {
            const c = cell;
            translateElement(c, value[keys[indexCell]]);
        }

        indexCell++;
    }
}

function changeTextContent(elem, value) {
    if (elem.childElementCount > 0 && elem.firstElementChild.tagName === "SPAN") {
        elem.firstElementChild.textContent = value;
    }
    else {
        elem.textContent = value;
    }
}

function changeInnerHTML(elem, value) {
    if (elem.childElementCount > 0 && elem.firstElementChild.tagName === "SPAN") {
        elem.firstElementChild.innerHTML = value;
    }
    else {
        elem.innerHTML = value;
    }
}

function hasHTML(value) {
    const regexHTML = /<\/?[a-z][\s\S]*>/i;

    return regexHTML.test(value);
}

function createNavbar() {
    const reference = document.querySelector("#content");
    let navbar = document.createElement("div");

    navbar.setAttribute("id", "navbar");
    reference.parentNode.insertBefore(navbar, reference);

    navbar.innerHTML =
        `null`;
}

function init() {

    createNavbar();

    const switcher = document.getElementById('lang-switcher');
    switcher.value = i18nDefaultLang;
    
    translatePage(i18nDefaultLang);
  
    switcher.addEventListener('change', (e) => {
        const selectedLang = e.target.value;
        translatePage(selectedLang);
    });
}

document.addEventListener('DOMContentLoaded', init);