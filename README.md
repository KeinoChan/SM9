# SM9
Bachelor's thesis- Collaborative Generation of SM9 Private Key



- [x] Finish TCP communication, with the received username from InteractiveButton.
- [x] Remove unused class, such as PKG1&PKG2.

- Two Threads is created in Main.java: Server and Client, as the two have to use the same SM9Curve and Paillier, which involve random variables/fields.

1. Run the Main.java in SM9-TCP, and the server start listen to  Socket.
2. Run the InteractiveButton app, the interface showed with a text line and a send button(named BUTTON).
3. Input the username in the App, click the Button, the App start connect the Server in the Main.java via Socket. "`This sentence is printed when the button is clicked`" is printed in the AndroidStudio run console tab. Also the Server console print "connected from /`ip and port`", and received username from the App input.
4. Server then start the Private Key generation via Client(Another Thread in Main.java), and key verification proceed.



To be honest, this is a terrible programming, the Client just start after the Server in a specified time, which means when you run the Main.java, and then run the App, if the time from the Server start to you click the Button after username input exceeds the specified time, the code would stuck and there's nothing you can do but repeat the procedure.
