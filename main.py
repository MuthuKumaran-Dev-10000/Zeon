import time

def fibonacci(n):
    a, b = 0, 1
    while n > 0:
        print(a, end=" ")
        temp = a
        a = b
        b = temp + b
        n -= 1
    print()


start_time = time.perf_counter()
fibonacci(10)
end_time = time.perf_counter()
x = (10+5)+2/5
print(x)
print(f"Execution Time (Python): {end_time - start_time:.10f} seconds")
