var frase = "La ruta nos aporto otro paso natural";

function isPalindrome(str) {
  if (str.length < 0) return false;
  
  var arrayStr = str.toLowerCase().split("").filter(isNotSpace);
  return arrayStr.join("") === arrayStr.reverse().join("");
}

function isNotSpace(str) {
  return str !== " ";
}

function checkPalindrome() {
  var text = document.getElementById("palindrome").value;
  var p;

  if (isPalindrome(text)) {
    p = getPalindromeParagraph(text, true, "green");
  } else {
    p = getPalindromeParagraph(text, false, "red");
  }
  document.body.appendChild(p);
}

function getPalindromeParagraph(content, bool, color) {
  var p = document.createElement('p');
  p.innerHTML = content + ": "+ (bool ? "Si" : "No");
  p.style.color = color;
  return p;
}

console.log(isPalindrome(frase));
console.log(isPalindrome("menem"));
console.log(isPalindrome("no"));