import mysql.connector


my_database = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="",
    database="project"
)


def main():
    my_cursor = my_database.cursor()

    my_cursor.execute("INSERT INTO `route`(`highway_id`, `meters`, `start`, `end`, `vehicle_id`, `timestamp`, `status`) VALUES ('1','5832','18.49540089,73.95350','18.49554334,74.01062','1','now()','nobill')")


if __name__ == '__main__':
    main()
