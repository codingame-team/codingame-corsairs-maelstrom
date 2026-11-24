import sys
import math
from collections import namedtuple
from dataclasses import dataclass

# Navigate the treacherous currents of the Maelstrom! Plunder floating treasure chests, manage your ship's cargo inertia, and blast enemy corsairs back into the whirlpool.


@dataclass
class Pos:
	x: int
	y: int

	def distance_to(self, other: 'Pos') -> float:
		return math.hypot(self.x - other.x, self.y - other.y)

@dataclass
class Ship:
	id: int
	owner: int
	pos: Pos
	gold: int
	cooldown: int
	action: str = ''


@dataclass
class Chest:
	id: int
	pos: Pos
	gold: int


# width: Width of the sea sector (1600)
# height: Height of the sea sector (900)
# league_level: Current league level (1=Wood 2, 2=Wood 1, 3=Bronze+)
width, height, league_level = [int(i) for i in input().split()]
# my_port_x: X coordinate of your Home Port
# my_port_y: Y coordinate of your Home Port
# opp_port_x: X coordinate of opponent Home Port
# opp_port_y: Y coordinate of opponent Home Port
# maelstrom_x: X coordinate of the central Maelstrom (800)
# maelstrom_y: Y coordinate of the central Maelstrom (450)
my_port_x, my_port_y, opp_port_x, opp_port_y, maelstrom_x, maelstrom_y = [int(i) for i in input().split()]
my_port = Pos(my_port_x, my_port_y)
opp_port = Pos(opp_port_x, opp_port_y)
maelstrom = Pos(maelstrom_x, maelstrom_y)

# game loop
while True:
	ship_count = int(input())  # Total number of ships
	ships = []
	for i in range(ship_count):
		# ship_id: Unique identifier of the ship
		# owner: 0 if you own this ship (listed first), 1 if owned by opponent
		# x: X coordinate of the ship
		# y: Y coordinate of the ship
		# vx: Horizontal velocity of the ship
		# vy: Vertical velocity of the ship
		# gold: Amount of gold currently carried by the ship (increases ship mass!)
		# cooldown: Turns remaining before this ship can FIRE its cannons again
		ship_id, owner, x, y, vx, vy, gold, cooldown = [int(j) for j in input().split()]
		ships.append(Ship(ship_id, owner, Pos(x, y), gold, cooldown))

	chest_count = int(input())  # Number of active floating treasure chests
	chests = []
	for i in range(chest_count):
		# chest_id: Unique identifier of the treasure chest
		# cx: X coordinate of the treasure chest
		# cy: Y coordinate of the treasure chest
		# chest_gold: Gold value of the treasure chest
		chest_id, cx, cy, chest_gold = [int(j) for j in input().split()]
		chests.append(Chest(chest_id, Pos(cx, cy), chest_gold))

	chests.sort(key=lambda c: c.gold, reverse=True)  # Sort chests by gold value (descending)
	for chest in chests:
		closest_ship = min(ships, key=lambda s: s.pos.distance_to(chest.pos))  # Find the closest ship to the chest
		closest_ship.action = f"SAIL {chest.pos.x} {chest.pos.y} 100"  # Sail towards the chest with full power

	for ship in ships:
		if not ship.action and ship.gold > 0:
			# If the ship has gold and no action assigned, sail back to the home port
			ship.action = f"SAIL {my_port.x} {my_port.y} 100"
		print(ship.action)  # Output the action for each ship

