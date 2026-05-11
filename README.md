# Sequence

This repository contains Java and Python implementations of the Sequence board game.

## Game Rules

Read the full rules in [rules.txt](rules.txt).

## Running the Python version of the program

The Python version is a terminal-based two-player game.

1. Install Python 3 if it is not already installed.
2. Install the required color output package:

   ```powershell
   pip install colorama
   ```

3. Change into the Python project folder:

   ```powershell
   cd Python
   ```

4. Start the game:

   ```powershell
   python Sequence.py
   ```

Follow the prompts in the terminal. Player moves are recorded in `Python/moves.txt` while the game is running.

## Running the Java version of the program

### Windows

Download and run the `.msi` installer from the [latest release](https://github.com/DMFriends/wordle/releases/latest).

When you run the latest `.msi`, it uninstalls previous versions of the app from your device.

### macOS and Linux

Linux and macOS packages are built by GitHub Actions when a GitHub Release is
published. The release workflow produces:

- Linux: `.deb`
- macOS: `.dmg`

You can download those packages from the [latest release](https://github.com/DMFriends/wordle/releases/latest). You may need to manually uninstall previous versions.
