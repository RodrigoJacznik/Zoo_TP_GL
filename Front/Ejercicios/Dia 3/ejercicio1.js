var valores = [true, 5, false, "hola", "adios", 2];

function isString(str) {
  return typeof str === "string";
}

function isBoolean(bool) {
  return typeof bool === "boolean";
}

function isNumber(num) {
  return typeof num === "number";
}

function max(previusMax, currentVal) {
  if (previusMax >= currentVal) {
    return previusMax;
  }
  return currentVal;
}

function returnTrue(previus, current) {
  return previus || current;
}

function returnFalse(previus, current) {
  return previus && current;
}

function sum(previus, current) {
  return previus + current;
}

function rest(previus, current) {
  return previus - current;
}

function mul(previus, current) {
  return previus * current;
}

function div(previus, current) {
  return previus / current;
}


// ejercicio 1.a
console.log(valores.filter(isString).reduce(max));

// ejercicio 1.b
console.log(valores.filter(isBoolean).reduce(returnTrue));
console.log(valores.filter(isBoolean).reduce(returnFalse));

// ejercicio 1.c
console.log(valores.filter(isNumber).reduce(sum));
console.log(valores.filter(isNumber).reduce(rest));
console.log(valores.filter(isNumber).reduce(mul));
console.log(valores.filter(isNumber).reduce(div));