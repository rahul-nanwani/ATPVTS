import csv

import mysql.connector


my_database = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="",
    database="project"
)


def main():
    my_cursor = my_database.cursor()
    vehicle_id = 1

    with open('route.csv', 'r') as f:
        route = csv.reader(f)
        f.close()

    for coordinate in route:
        my_cursor.execute(f"INSERT INTO `coordinates`(`longitude`, `latitude`, `timestamp`, `vehicle_id`) VALUES('{coordinate[0]}', '{coordinate[1]}', 'now()', '{vehicle_id}')")


if __name__ == '__main__':
    main()
