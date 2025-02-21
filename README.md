Zeon programming language documentation

variable declaration :

        let varname : value;
        ?? let is optional and when redeclaration it is not necessary

Input : 

        let varname  :  input("prompt");

comments :
        
        ?? comments here 

list : 

        let varname : [1,2,3,4];

dictionary :

        let varname : {key:val};

print statement:

        print("values _escapesequence");
        print("value"*repeatation value);
        print("$val is here");

function declaration :
        
        fn funcname (p1,p2,p3){
            ?? statements here
            return value;
        }

function call :

        funcname(p1,p2,p3);

conditional statements : 

        if(condition){
            ??statement here
        }elseif(condition){
            ??statement here
        }else{
            ??statement here
        }

looping statements :

        while(condition){
            ?? statements here
            let var : var+1; ?? increment must be like redeclaration
        }

        for(let i : 0, i <= 5, let i : i + 1) {
                print(i+"_n");
        } 

        ?? let is optional in variable declaration and increment

Type checking :

        type varname;

Type casting :

        string(value);
        number(value);
        list(value);
        
using java/python code in zeon : 

        java ` 

        import java.util.*;

        Scanner sc = new Scanner(System.in);
        String name  = sc.nextLine();
        System.out.println("hi "+name);

        `

        python `

        n = input("hi enter your age : ")
        print("the user age is : "+n)

        `

FileHandiling : 

        with ("filename.txt", w) as VAR {
            write(VAR, "Hello, Zeon!");
            write(VAR, "This is a test.");
        }
        with ("filename.txt", r) as VAR {
            print(VAR); 
        }

Exception handling Try Catch :

        try {
            ??statements
        } catch(e) {
            print(e); 
            print("_n");
        } finally {
            print("This will always execute._n");
        }

Switch statement : (unfortunately expressions is not added currently)

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