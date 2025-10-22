fun main() {
    val chatRoom = ChatRoom()

    val alex = ChatUser("Alex", chatRoom)
    val lily = ChatUser("Lily", chatRoom)
    val sully = ChatUser("Sully", chatRoom)

    chatRoom.addUser(alex)
    chatRoom.addUser(lily)
    chatRoom.addUser(sully)

    println("\n=== Chat Session ===")
    alex.send("wassuuuuup!")
    lily.send("Hi Alex!")
    sully.send("yo!")
    lily.send("Hey sully!!")
}
