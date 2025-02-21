class letInterpreter:
    def __init__(self):
        self.variables = {}
        self.current_line = 0
        self.program_lines = []

    def run(self):
        self.read_input()
        self.execute_program()

    def read_input(self):
        print("Enter your let++ program (type VARATA to end input):")
        while True:
            line = input()
            if line.strip() == "VARATA":
                break
            self.program_lines.append(line)

    def execute_program(self):
        while self.current_line < len(self.program_lines):
            line = self.program_lines[self.current_line].strip()

            if line.startswith("let "):
                self.parse_variable_definition(line)
            elif line.startswith("print "):
                self.parse_print_statement(line)
            elif line.startswith("arokara "):
                self.parse_for_loop(line)
            elif line.startswith("if "):
                self.parse_if_statement(line)
            elif line.startswith("elseif "):
                self.parse_elseif_statement(line)
            elif line.startswith("else "):
                self.parse_else_statement(line)
            elif line.startswith("?? "):
                self.parse_comment(line)
            else:
                print(f"Error: Unrecognized statement at line {self.current_line + 1}")
                break
            
            self.current_line += 1

    def parse_variable_definition(self, line):
        _, rest = line.split("let ", 1)
        var_name, value = rest.split(":")
        var_name = var_name.strip()
        value = value.strip().strip(";")
        self.variables[var_name] = value

    def parse_print_statement(self, line):
        _, rest = line.split("print ", 1)
        expression = rest.strip().strip(";")
        result = self.evaluate_expression(expression)
        print(result)

    def parse_for_loop(self, line):
        _, condition = line.split("arokara ", 1)
        condition = condition.strip().rstrip(";")
        loop_var, range_val = condition.split("..")
        start, end = int(loop_var), int(range_val.split("{")[0].strip())
        statement = range_val.split("{")[1].strip().rstrip("}")
        
        for i in range(start, end + 1):
            self.variables[loop_var] = str(i)
            self.execute_statement(statement)

    def parse_if_statement(self, line):
        _, rest = line.split("if ", 1)
        condition, block = rest.split("iukumo{", 1)
        condition = condition.strip()
        block = block.strip().rstrip("}")
        
        if self.evaluate_condition(condition):
            self.execute_statement(block)

    def parse_elseif_statement(self, line):
        _, rest = line.split("elseif ", 1)
        condition, block = rest.split("iukumo{", 1)
        condition = condition.strip()
        block = block.strip().rstrip("}")
        
        if not self.previous_condition_evaluated() and self.evaluate_condition(condition):
            self.execute_statement(block)

    def parse_else_statement(self, line):
        _, block = line.split("else iukumo{", 1)
        block = block.strip().rstrip("}")
        
        if not self.previous_condition_evaluated():
            self.execute_statement(block)

    def parse_comment(self, line):
        comment = line.strip("?? ").rstrip()
        if comment.endswith("*"):
            print(f"\033[91m{comment[:-1]}\033[0m")  # Print in red
        elif comment.endswith("|"):
            print(f"\033[92m{comment[:-1]}\033[0m")  # Print in green
        else:
            print(comment)

    def evaluate_expression(self, expression):
        try:
            return eval(expression, {}, self.variables)
        except Exception as e:
            print(f"Error evaluating expression: {e}")
            return None

    def evaluate_condition(self, condition):
        var_name, operator, value = self.extract_condition_parts(condition)
        if operator == "==":
            return self.variables.get(var_name) == value
        elif operator == "!=":
            return self.variables.get(var_name) != value
        elif operator == "<":
            return int(self.variables.get(var_name)) < int(value)
        elif operator == ">":
            return int(self.variables.get(var_name)) > int(value)
        elif operator == "<=":
            return int(self.variables.get(var_name)) <= int(value)
        elif operator == ">=":
            return int(self.variables.get(var_name)) >= int(value)
        else:
            return False

    def extract_condition_parts(self, condition):
        operators = ["==", "!=", "<=", ">=", "<", ">"]
        for op in operators:
            if op in condition:
                var_name, value = condition.split(op)
                return var_name.strip(), op, value.strip()
        return "", "", ""

    def previous_condition_evaluated(self):
        return False  # Placeholder for future implementation

    def execute_statement(self, statement):
        # Placeholder for future implementation
        pass

if __name__ == "__main__":
    interpreter = letInterpreter()
    interpreter.run()
