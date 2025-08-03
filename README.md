## 🌟 About Zeon

**Zeon** is a **compact, efficient, and beginner-friendly programming language** designed for simplicity and power. Built with a focus on readability and developer productivity, Zeon combines the elegance of modern syntax with the flexibility to embed Java and Python code directly.

🛠️ **Fully Open Source** and community-driven, Zeon is ideal for:

- Rapid prototyping
- Learning programming concepts
- Lightweight applications
- Embedding scripting in larger systems

Whether you're a curious beginner or an experienced developer looking for a lightweight tool, **Zeon** is here to make coding intuitive and fun.

# Zeon Programming Language Documentation

## 🧠 Variable Declaration

```zeon
let varname : value;
```
- `let` is **optional**.
- For **re-declaration**, `let` is not required.

## ⌨️ Input

```zeon
let varname : input("prompt");
```

## 💬 Comments

```zeon
?? comments here
```

## 📋 List

```zeon
let varname : [1, 2, 3, 4];
```

## 🧾 Dictionary

```zeon
let varname : {key: val};
```

## 🖨️ Print Statement

```zeon
print("values _escapesequence");
print("value" * repeatation_value);
print("$val is here");
```

## 🔧 Function Declaration

```zeon
fn funcname(p1, p2, p3) {
    ?? statements here
    return value;
}
```

### Function Call

```zeon
funcname(p1, p2, p3);
```

## 🔀 Conditional Statements

```zeon
if(condition) {
    ?? statement here
} elseif(condition) {
    ?? statement here
} else {
    ?? statement here
}
```

## 🔁 Looping Statements

### While Loop

```zeon
while(condition) {
    ?? statements here
    let var : var + 1;  // increment (re-declaration style)
}
```

### For Loop

```zeon
for(let i : 0, i <= 5, let i : i + 1) {
    print(i + "_n");
}
```
- `let` is optional in **initialization** and **increment**.

## 🧪 Type Checking

```zeon
type varname;
```

## 🔄 Type Casting

```zeon
string(value);
number(value);
list(value);
```

## 🔗 Using Java / Python Code in Zeon

### Java Block

```zeon
java `

import java.util.*;

Scanner sc = new Scanner(System.in);
String name = sc.nextLine();
System.out.println("hi " + name);

`
```

### Python Block

```zeon
python `

n = input("hi enter your age : ")
print("the user age is : " + n)

`
```

## 📂 File Handling

```zeon
with ("filename.txt", w) as VAR {
    write(VAR, "Hello, Zeon!");
    write(VAR, "This is a test.");
}

with ("filename.txt", r) as VAR {
    print(VAR);
}
```

## ⚠️ Exception Handling (Try-Catch-Finally)

```zeon
try {
    ?? statements
} catch(e) {
    print(e);
    print("_n");
} finally {
    print("This will always execute._n");
}
```

## 🧭 Switch Statement

> ⚠️ Currently, **expressions are not supported** in `case`.

```zeon
let num : 2;

switch(num) {
    case 1 {
        print("One");
    }
    case 2 {
        print("Two");
    }
    case 3 {
        print("Three");
    }
    default {
        print("Not 1, 2, or 3");
    }
}
```

