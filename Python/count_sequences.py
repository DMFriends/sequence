# Function for checking for winning positions on the board
def count_sequences(player_id, player_move_locations):
	num_sequences = count_row_seq(player_id, player_move_locations)
	num_sequences += count_column_seq(player_id, player_move_locations)
	num_sequences += count_se_diag_seq(player_id, player_move_locations)
	num_sequences += count_sw_diag_seq(player_id, player_move_locations)
		
	return num_sequences


# Function for finding sequences in rows (for checking winning positions)
def count_row_seq(player_id, player_move_locations):
	row = 0
	num_sequences = 0
	while row <= 5:
		column = 0
		length = 0
		while column <= 7:
			if [row, column] in player_move_locations[player_id]:
				length += 1
			else:
				length = 0
			if length >= 4:
				num_sequences += 1
				break
			column += 1
		row += 1
		
	return num_sequences


# Function for finding sequences in columns (for checking winning positions)
def count_column_seq(player_id, player_move_locations):
	column = 0
	num_sequences = 0
	while column <= 7:
		row = 0
		length = 0
		while row <= 5:
			if [row, column] in player_move_locations[player_id]:
				length += 1
			else:
				length = 0
			if length >= 4:
				num_sequences += 1
				break
			row += 1
		column += 1
			
	return num_sequences


# Function for finding sequences in diagonals going in SE direction (\) (for checking winning positions)
def count_se_diag_seq(player_id, player_move_locations):
	diag_id = -2
	num_sequences = 0
	while diag_id <= 4:
		length = 0
		if diag_id < 0:
			row = -diag_id
			column = 0
		else:
			row = 0
			column = diag_id
		diag_id += 1
		while row <= 5 and column <= 7:
			if [row, column] in player_move_locations[player_id]:
				length += 1
			else:
				length = 0
				
			if length >= 4:
				num_sequences += 1
				break
				
			row += 1
			column += 1
				
	return num_sequences


# Function for finding sequences in diagonals going in SW direction (/) (for checking winning positions)
def count_sw_diag_seq(player_id, player_move_locations):
	diag_id = -2
	num_sequences = 0
	while diag_id <= 4:
		length = 0
		if diag_id < 0:
			row = -diag_id
			column = 7
		else:
			row = 0
			column = 7 - diag_id
		diag_id += 1
		while row <= 5 and column >= 0:
			if [row, column] in player_move_locations[player_id]:
				length += 1
			else:
				length = 0
				
			if length >= 4:
				num_sequences += 1
				break
				
			row += 1
			column -= 1
			
	return num_sequences


def count_num_sequences(player_id, player_move_locations):
	if player_id == 1:
		current_num_sequences_1 = count_sequences(player_id, player_move_locations)

		if current_num_sequences_1 == 0 or current_num_sequences_1 > 1:
			print("Player " + str(player_id) + " currently has " + str(current_num_sequences_1) + " Sequences!")
		if current_num_sequences_1 == 1:
			print("Player " + str(player_id) + " currently has 1 Sequence!")

		current_num_sequences_2 = count_sequences(3 - player_id, player_move_locations)

		if current_num_sequences_2 == 0 or current_num_sequences_2 > 1:
			print("Player " + str(3 - player_id) + " currently has " + str(current_num_sequences_2) + " Sequences!")
		if current_num_sequences_2 == 1:
			print("Player " + str(3 - player_id) + " currently has 1 Sequence!")

	if player_id == 2:
		current_num_sequences_1 = count_sequences(3 - player_id, player_move_locations)

		if current_num_sequences_1 == 0 or current_num_sequences_1 > 1:
			print("Player " + str(3 - player_id) + " currently has " + str(current_num_sequences_1) + " Sequences!")
		if current_num_sequences_1 == 1:
			print("Player " + str(3 - player_id) + " currently has 1 Sequence!")

		current_num_sequences_2 = count_sequences(player_id, player_move_locations)

		if current_num_sequences_2 == 0 or current_num_sequences_2 > 1:
			print("Player " + str(player_id) + " currently has " + str(current_num_sequences_2) + " Sequences!")
		if current_num_sequences_2 == 1:
			print("Player " + str(player_id) + " currently has 1 Sequence!")
