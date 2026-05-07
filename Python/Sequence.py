import time
import random
import sys
import os
import datetime
from colorama import Fore, Style
from table import *
from count_sequences import *

board = [
    ["2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D"],
    ["AS", "5C", "4C", "3C", "2C", "AH", "KH", "10D"],
    ["KS", "6C", "5H", "4H", "3H", "2H", "QH", "QD"],
    ["QS", "7C", "6H", "7H", "8H", "9H", "10H", "KD"],
    ["10S", "8C", "9C", "10C", "QC", "KC", "AC", "AD"],
    ["9S", "8S", "7S", "6S", "5S", "4S", "3S", "2S"],
]

available_cards = []

i = 0

while i < 6:
    available_cards.extend(board[i])
    i += 1

available_cards.extend(["JD", "JC", "JH", "JS"])

player_move_locations = [[], [], []]
player_cards = [[], [], []]
cards_played = []
cards_played_with_jack = []
dead_cards = []
num_sequences = [0, 0, 0]

# global new_card

# Deal out cards for both players and remove those cards from "available_cards"
player_cards[1] = random.sample(available_cards, k=7)
for card in player_cards[1]:
    available_cards.remove(card)
player_cards[2] = random.sample(available_cards, k=7)
for card in player_cards[2]:
    available_cards.remove(card)


# Function for finding a card's location on the board
def find_card(card):
    row = 0

    while True:
        if card in board[row]:
            column = board[row].index(card)
            return [row, column]

        row += 1

        if row == 6:
            return None


# Function for making a move with Jacks (wild cards)
def make_move_with_jack(player_id, player_move):
    # Move validation
    while True:
        print("You moved with a Jack.")
        move_with_jack = str(
            input("Find an unoccupied space on the board and enter the card's abbreviation, ex. 2D or KS: "))

        card_location = find_card(move_with_jack)

        if move_with_jack in player_cards[player_id]:
            print("You already have this card in your hand, so it makes more sense to play"
                  "that card instead of the Jack. Please try again.")
        elif move_with_jack in cards_played:
            print("That space is already occupied. Please choose another space.")
        else:
            break

    cards_played.append(player_move + " - " + move_with_jack)

    cards_played_with_jack.append(move_with_jack)

    dead_cards.append(move_with_jack)

    # Look for dead card in available_cards
    if move_with_jack in available_cards:
        available_cards.remove(move_with_jack)

    # Look for dead card in the other player's hand
    if move_with_jack in player_cards[3 - player_id]:
        player_cards[3 - player_id].remove(move_with_jack)
        if len(available_cards) > 0:
            new_card = random.sample(available_cards, k=1)
            player_cards[3 - player_id].append(new_card[0])
            available_cards.remove(new_card[0])

    finalize_move(player_id, player_move, card_location)


# Function for making a player's move
def make_move(player_id):
    # player_id = 1 or 2

    if player_id == 1:
        print(Fore.RED + "Player 1's turn!")
        print(Style.RESET_ALL)
        count_num_sequences(1, player_move_locations)

    if player_id == 2:
        print(Fore.BLUE + "Player 2's turn!")
        print(Style.RESET_ALL)
        count_num_sequences(2, player_move_locations)

    table = make_table(board, player_move_locations)
    time.sleep(0.5)
    print(table)

    print("Player " + str(player_id) + ", you have the following cards:")
    time.sleep(2)
    print(*player_cards[player_id], sep=', ')

    time.sleep(0.5)
    if len(cards_played) >= 1 and "J" not in cards_played[-1]:
        print("\nPlayer " + str(3 - player_id) + "'s last move was '" + cards_played[-1] + "'.")
    if len(cards_played_with_jack) >= 1 and "J" in cards_played[-1]:
        print("\nPlayer " + str(3 - player_id) + "'s last move was '" + cards_played[-1] + "'.")

    # Move validation
    while True:
        player_move = str(input("\nPlayer " + str(player_id) + ", what is your move? "))
        card_location = find_card(player_move)

        with open("moves.txt", "a") as moves:
            moves.write("Player " + str(player_id) + ": " + "%s\n" % player_move)

        if (player_move not in player_cards[player_id] or player_move in available_cards or
                player_move in player_cards[3 - player_id]):
            print("You do not have that card or you entered an invalid card. Please try again.")
        else:
            break

    if "J" in player_move:
        make_move_with_jack(player_id, player_move)
    else:
        cards_played.append(player_move)
        finalize_move(player_id, player_move, card_location)

    check_win(player_id)


# Function for finalizing a player's move
def finalize_move(player_id, player_move, card_location):
    # Remove the card that the player just played from their hand
    global new_card
    player_cards[player_id].remove(player_move)

    # Saves the location of the player's move on the board to player_move_locations
    player_move_locations[player_id].append(card_location)

    row = card_location[0]
    column = card_location[1]

    # Add a star to the corresponding square on the board
    board[row][column] = "X"

    # The player receives a new card from available_cards after each move (only if available_cards is not empty)
    if len(available_cards) > 0:
        new_card = random.sample(available_cards, k=1)

    # Remove the new card from available_cards
    available_cards.remove(new_card[0])

    # Add the new card to the player's hand
    player_cards[player_id].append(new_card[0])


# Function to check if any player has won
def check_win(player_id):
    current_num_sequences = count_sequences(player_id, player_move_locations)
    if current_num_sequences >= 4:
        os.system("cls || clear")
        time.sleep(0.5)
        table = make_table(board, player_move_locations)
        print(table)
        print("Congratulations, Player " + str(player_id) + "! You won!")
        print("Player " + str(3 - player_id) + " loses with " +
              str(count_sequences(3 - player_id, player_move_locations)) + " sequences.")
        sys.exit()


os.system("cls || clear")

print("Welcome to Sequence!")
print("To see the rules, visit the 'rules.txt' file. Please note that the rules in that file have been"
      " modified to suit your needs for playing the online\nversion of this game.")
print("To see the actual version of the rules, visit the 'Sequence Rules.png' file.")
print("Player 1's moves on the board are marked with red stars, and Player 2's"
      " moves on the board are marked with blue stars.\n")

str(input("It is now Player 1's turn. Press enter when ready for Player 1's move. "))

os.system("cls || clear")

with open("moves.txt", "r+") as moves:
    moves.truncate(0)

time.sleep(1)

while True:
    make_move(1)
    os.system("cls || clear")
    str(input(
        "It is now Player 2's turn. Please switch with Player 2 and press enter when ready for Player 2's move. "))
    os.system("cls || clear")
    make_move(2)
    os.system("cls || clear")
    str(input(
        "It is now Player 1's turn. Please switch with Player 1 and press enter when ready for Player 1's move. "))
    os.system("cls || clear")
    continue
