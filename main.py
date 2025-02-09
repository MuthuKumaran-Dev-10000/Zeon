import time

def greet(color, min_val, sec):
    while min_val < sec:
        print(f"{color} Hello, Zeon! _reset")
        min_val = 3  # Simulating the reassignment in Zeon

    print(min_val + sec)

# Start timer
start_time = time.time()

# Execute functions
greet("_blue hi guys", 1, 2)
greet("_red", 1, 3)

# End timer and print execution time in milliseconds
end_time = time.time()
execution_time = (end_time - start_time) * 1000  # Convert to milliseconds
print(f"Execution Time: {execution_time:.2f} ms")
