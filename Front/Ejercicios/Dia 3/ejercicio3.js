function Invoce(client, elements) {
  this.client = client;
  this.elements = elements;
}

Invoce.prototype.getTotal = function () {
  return this.elements
    .map(function (element) {
      return element.value * element.amount;
    })
    .reduce(function (currentTotal, elementSubTotal) {
      return currentTotal + elementSubTotal;
    });
};

function Element(amount, description, value) {
  this.amount = amount;
  this.description = description;
  this.value = value;
}

function Client(name, type) {
  this.name = name;
  this.type = type;
}

var elements = [new Element(10, "vasos", 15.50), new Element(3, "platos", 30), new Element(5, "tenedores", 5.75)];
var client = new Client("pepe", "inscripto");
var invoce = new Invoce(client, elements);

console.log(invoce.getTotal());