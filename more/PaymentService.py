import mysql.connector

my_database = mysql.connector.connect(
    host="localhost",
    user="root",
    passwd="",
    database="project"
)


def generate_bill(routes, vehicle_id):
    bills = []
    total_km = 0
    for route in routes:
        status = search(bills, route[1])
        if status >= 0:
            bills[status][1] += route[2]
        else:
            bill = [route[1], route[2]]
            bills.append(bill)
    for bill in bills:
        print(bill)
        km = bill[1] / 1000
        total_km += km
        if km > 2:
            toll = km * 10
            insert_bill(bill[0], km, toll, vehicle_id)
            change_status(bill[0], vehicle_id)


def search(bills, route_name):
    for x in range(0, len(bills), 1):
        if bills[x][0] == route_name:
            return x
    return -1


def change_status(highway_id, vehicle_id):
    cursor = my_database.cursor()
    try:
        # Execute the SQL command
        cursor.execute(f"UPDATE route SET status='billed' WHERE highway_id='{highway_id}' AND vehicle_id='{vehicle_id}'")
        # Commit your changes in the database
        my_database.commit()
    except Exception as e:
        # Rollback in case there is any error
        print(e)
        my_database.rollback()


def insert_bill(highway, distance, toll, vehicle_id):
    cursor = my_database.cursor()
    try:
        # Execute the SQL Command
        cursor.execute(f"INSERT INTO bills (highway_id,meters,toll,vehicle_id) VALUES ('{highway}','{distance}','{toll}','{vehicle_id}')")
        # Commit your changes in the database
        my_database.commit()
        return True
    except mysql.connector.Error as e:
        # Rollback in case there is any error
        print(e)
        my_database.rollback()
        return False


def main():
    print("Payment Running...")
    my_cursor = my_database.cursor()

    my_cursor.execute("SELECT DISTINCT vehicle_id FROM route")

    vehicles = my_cursor.fetchall()

    for vehicle in vehicles:
        vehicle_id = vehicle[0]
        my_cursor.execute(f"SELECT * FROM route WHERE vehicle_id='{vehicle_id}' AND status='nobill' ORDER BY route.timestamp ASC")
        routes = my_cursor.fetchall()
        count = my_cursor.rowcount
        if count > 0:
            try:
                generate_bill(routes, vehicle_id)
                print(f"Bill Generated for Vehicle ID: {vehicle_id}, Route: {routes}")
            except Exception as e:
                print(e)


if __name__ == '__main__':
    main()
