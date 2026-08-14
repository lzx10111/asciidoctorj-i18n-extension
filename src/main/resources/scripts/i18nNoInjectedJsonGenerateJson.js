import nullTranslations from "./locales/null/translation.json" with { type: "json" };

const i18nDefaultLang = "null";
const i18nPrefix = "null";
const i18nKeyPrefix = "null";

const pathParameters = "path_parameters";
const queryParameters = "query_parameters";
const responsefields = "response_fields";

function loadTranslations(lang) {
    switch (lang) {
        case "null":
            return nullTranslations;

        default:
            return nullTranslations;
    }
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
    switch (elem.tagName) {
      case "DIV":
        translateDiv(elem, value);
        break;
      case "TABLE":
        translateTable(elem, value);
        break;
      case "UL":
        translateUl(elem, value);
        break;
      case "SELECT":
        translateSelect(elem, value);
        break;
    
      default:
        translateDefault(elem, value);
        break;
    }
}

function translateDiv(elem, value) {
    if (hasChildWithoutText(elem)) {
        translateElement(elem.firstElementChild, value);
    }
    else {
        translateDefault(elem, value);
    }
}

function translateTable(elem, value) {
    const elements = elem.getElementsByTagName("tr");
    let index = 0;

    for (const e of elements) {
        translateTr(e, value[index], index);
        index++;
    }
}

function translateUl(elem, value) {
    const elements = elem.getElementsByTagName("li");
    let index = 0;
    const keys = Object.keys(value[index]);

    for (const e of elements) {
        translateElement(e, value[index][keys[0]]);
        index++;
    }
}

function translateSelect(elem, value) {
    const elements = elem.getElementsByTagName("option");
    let index = 0;

    for (const e of elements) {
        const keys = Object.keys(value[index]);
        translateElement(e, value[index][keys[0]]);
        index++;
    }
}

function translateDefault(elem, value) {
    if (hasHTML(value)) {
        changeInnerHTML(elem, value);
    }
    else {
        changeTextContent(elem, value);
    }
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

function hasChildWithoutText(element) {
  const hasChildren = element.children.length > 0;
  
  let hasOwnText = false;
  for (const node of element.childNodes) {
    if (node.nodeType === Node.TEXT_NODE) {
      if (node.textContent.trim() !== "") {
        hasOwnText = true;
        break;
      }
    }
  }
  
  return hasChildren && !hasOwnText;
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

function getGeneratedJson() {
    setRestDocsElements(pathParameters);
    setRestDocsElements(queryParameters);
    setRestDocsElements(responsefields);

    return JSON.stringify(getI18nObject(), null, 4);
}

function saveGeneratedJson() {
    const jsonString = getGeneratedJson();
    const blob = new Blob([jsonString], { type: "application/json" });
    const url = URL.createObjectURL(blob);

    const link = document.createElement("a");
    link.href = url;
    link.target = "_blank";

    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function getI18nObject() {
    const elements = document.querySelectorAll('.' + i18nPrefix);
    const pathArray = getPaths(elements);
    let obj = joinPaths(pathArray);


    for (const elem of elements) {
        const key = getJsonKey(elem);
        const value = getValueFromElement(elem);

        setValue(obj, key, value);
    }

    return obj;
}

function setRestDocsElements(suffix) {
    const elements = document.querySelectorAll(`[id*=${suffix}]`);

    elements.forEach(e => {
        const table = e.parentElement.querySelector('table');
        const id = e.getAttribute("id");
        const i18nClass = getI18nClassFromId(id, suffix);

        e.classList.add(i18nClass[0], i18nClass[1] + "_title");
        table.classList.add(i18nClass[0], i18nClass[1] + "_table");
    }
    );
}

function getPaths(elements) {
    let pathArray = [];

    for (const elem of elements) {
        const key = getJsonKey(elem);
        pathArray.push(key);
    }

    return pathArray;
}

function setValue(obj, path, value) {
    const keys = path.split('.');
    let actualLevel = obj;

    for (let i = 0; i < keys.length - 1; i++) {
        const key = keys[i];

        if (!actualLevel[key] || typeof actualLevel[key] !== 'object') {
            actualLevel[key] = {};
        }

        actualLevel = actualLevel[key];
    }

    const lastKey = keys[keys.length - 1];
    actualLevel[lastKey] = value;

    return obj;
}

function joinPaths(pathArray) {
    let result = {};

    pathArray.forEach(path => {
        const keys = path.split('.');
        let actualLevel = result;

        keys.forEach((key) => {
            if (!actualLevel[key]) {
                actualLevel[key] = {};
            }

            actualLevel = actualLevel[key];
        });
    });

    return result;
}

function getKeysFromId(id, suffix) {
    const index = id.indexOf(suffix);
    const sub1 = id.substring(0, index).slice(1, -1).replaceAll("_", "-");
    const sub2 = id.substring(index, id.length).replaceAll("_", "-");
    let keysArray = [];

    keysArray.push(sub1);
    keysArray.push(sub2);

    return keysArray;
}

function getI18nClassFromId(id, suffix) {
    const keysArray = getKeysFromId(id, suffix);
    let classesArray = [];

    classesArray.push(i18nPrefix);
    classesArray.push(i18nKeyPrefix + keysArray[0] + "_" + keysArray[1]);

    return classesArray;
}

function getValueFromElement(elem) {
    switch (elem.tagName) {
        case "DIV":
            return getDivObject(elem);
        case "TABLE":
            return getRowsObjects(elem);
        case "UL":
            return getUnorderedListObject(elem);
        case "SELECT":
            return getSelectObject(elem);

        default:
            return getElementText(elem);
    }
}

function getElementText(element) {
    if (hasHTML(element.innerHTML)) {
        return element.innerHTML;
    }
    else {
        return element.textContent;
    }
}

function getDivObject(elem) {
    if (hasHTML(elem.innerHTML)) {
        return getValueFromElement(elem.firstElementChild);
    }
    else {
        return getValueFromElement(elem.textContent);
    }
}

function getUnorderedListObject(elem) {
    const elements = elem.querySelectorAll("li");
    let listItems = [];

    for (const e of elements) {
        listItems.push(getListItemObjects(e));
    }

    return listItems;
}

function getListItemObjects(listItem) {
    let obj = {};

    obj["item"] = getElementText(listItem);

    return obj;
}

function getSelectObject(select) {
    const elements = select.querySelectorAll("option");
    let options = [];

    for (const e of elements) {
        options.push(getOptionObjects(e));
    }

    return options;
}

function getOptionObjects(option) {
    let obj = {};

    obj[option.getAttribute("value")] = getElementText(option);

    return obj;
}

function getHeadObjects(columnsText) {
    let obj = {};

    let index = 0;
    for (const c of columnsText) {
        obj[columnsText[index].toLowerCase()] = columnsText[index];
        index++;
    }

    return obj;
}

function getRowsObjects(table) {
    const elements = table.querySelectorAll('tbody tr');
    const columnsText = getHeadText(table);
    let rows = [];

    rows.push(getHeadObjects(columnsText));

    for (const e of elements) {
        rows.push(getRowText(columnsText, e));
    }

    return rows;
}

function getHeadText(table) {
    const elements = table.querySelectorAll('thead tr th');
    let columns = [];

    for (const e of elements) {
        columns.push(getElementText(e));
    }

    return columns;
}

function getRowText(columnsText, row) {
    const elements = row.querySelectorAll('td p');
    let obj = {};

    let index = 0;
    for (const e of elements) {
        obj[columnsText[index].toLowerCase()] = getElementText(e);
        index++;
    }

    return obj;
}

function createNavbar() {
    const reference = document.querySelector("#content");
    let navbar = document.createElement("div");

    navbar.setAttribute("id", "navbar");
    reference.parentNode.insertBefore(navbar, reference);

    navbar.innerHTML =
        `null`;

    navbar.innerHTML = navbar.innerHTML + `<button id="i18n-generate-json">Generate JSON</button>`;

    const button = document.getElementById('i18n-generate-json');
    button.addEventListener('click', saveGeneratedJson);
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