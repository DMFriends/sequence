from typing import Any, List, Optional
from colorama import Fore, Style

def make_table(rows: List[List[Any]], player_move_locations) -> str:
    return_value = ""
    column_lengths: List[Any] = []
    
    # Populate "column_lengths"
    i = 0
    while i < len(rows):
        row = rows[i]
        j = 0
        while j < len(row):
            current_length = len(str(row[j])) + 2
            if i == 0:
                column_lengths.append(current_length)
            elif current_length > column_lengths[j]:
                column_lengths[j] = current_length
            j += 1
        i += 1

    # Top border
    return_value += make_top_border(column_lengths)

    # Data
    i = 0
    while i < len(rows):
        return_value += "\n" + make_row(rows[i], column_lengths, i, player_move_locations)
        i += 1        
        if i == len(rows):
            # If last row, no separator
            return_value += "\n" + make_bottom_border(column_lengths)
        else:
            # Separator
            return_value += "\n" + make_separator(column_lengths)

    return return_value

def make_row(row: List, column_lengths: List, row_id: Optional = -1, player_move_locations: Optional = None) -> str:
    return_value = "│"
    centered=True
    i = 0
    while i < len(row):
        if row_id != -1:
            location = [row_id, i]

            if "X" == str(row[i]):
                if location in player_move_locations[1]:
                    return_value += Fore.RED
                if location in player_move_locations[2]:
                    return_value += Fore.BLUE

        if centered:
            spaces_left = int((column_lengths[i] - len(str(row[i]))) / 2)
            spaces_right = column_lengths[i] - len(str(row[i])) - spaces_left
        else:
            spaces_left = 1
            spaces_right = column_lengths[i] - len(str(row[i])) - 1
        return_value += " " * spaces_left
        return_value += str(row[i])
        return_value += " " * spaces_right
        return_value += Style.RESET_ALL
        return_value += "│"
        
        # if current_length > column_lengths[i]:
        #     column_lengths[i] = current_length
        i += 1

    return return_value

def make_top_border(column_lengths) -> str:
    return_value = "┌"
    i = 0
    while i < len(column_lengths):
        j = 0
        while j < column_lengths[i]:
            return_value += "─"
            j += 1

        if i == len(column_lengths) - 1:
            return_value += "┐"
        else:
            return_value += "┬"
        i += 1

    return return_value

def make_separator(column_lengths) -> str:
    return_value = "├"
    i = 0
    while i < len(column_lengths):
        j = 0
        while j < column_lengths[i]:
            return_value += "─"
            j += 1

        if i == len(column_lengths) - 1:
            return_value += "┤"
        else:
            return_value += "┼"
        i += 1

    return return_value

def make_bottom_border(column_lengths) -> str:
    return_value = "└"
    i = 0
    while i < len(column_lengths):
        j = 0
        while j < column_lengths[i]:
            return_value += "─"
            j += 1

        if i == len(column_lengths) - 1:
            return_value += "┘"
        else:
            return_value += "┴"
        i += 1

    return return_value

# if __name__ == "__main__":
#     table = make_table(
#     rows=[
#         ["June 23", "9:00am", "Breakfast", "Cereal"],
#         ["June 23", "1:00pm", "Lunch", "Chicken Soup"],
#         ["June 23", "3:00pm", "Snack", "Some sort of snack"],
#         ["June 23", "6:00pm", "Dinner", "Chinese food"],
#     ],
#     labels=["Date", "Time", "Type of Meal", "Meal"],
#     centered=True
#     )

#     print(table)