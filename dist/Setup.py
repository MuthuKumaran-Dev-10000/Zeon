import os
import sys

def update_env_path():
    # Get the current directory of the executable
    current_dir = os.path.dirname(sys.executable)  # For .exe
    print(f"Current directory: {current_dir}")

    # Get the current PATH environment variable
    current_path = os.environ.get('PATH', '')
    
    # Add the current directory to the PATH if not already there
    if current_dir not in current_path:
        new_path = current_path + os.pathsep + current_dir
        os.environ['PATH'] = new_path
        print(f"Updated PATH: {os.environ['PATH']}")
    else:
        print("Directory already in PATH.")

if __name__ == "__main__":
    update_env_path()
    input("Press Enter to exit.")  # Keep window open if running in a terminal
