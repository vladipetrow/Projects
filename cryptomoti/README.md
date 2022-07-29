## How to start the app:

- Go to cryptomoti and open it with Intelij then open workProject1 then start the server.
- Go to imot4e-fe and open it with VS code 
- Write in terminal cd imot4e-fe, npm install --force
and then npm start.

- You are ready!


## Technical features
- JWT authentication - using JWT tokens for authorizing users and agencies. The token is
composed of user email and id which I use in some end points.
- REST API
- Multiple third-party API integrations(BTCPay server, Google maps, Mailgun)
- Exception handling and error messaging
- Password hashing - sha256.

## Idea of the App.

The project is about Real estate, posting real estate adds for sale or for rent ( for now the front end part
is to see how the app would work and look and is not fully connected with the back end part, working parts are
registration, login, subscription, making posts, sending emails ). There are two types
users - agency (which can post 5 ads and when subscribed up to 10) and ordinary users (which can post 3
ads and when subscribed up to 5). Users and Agencies can make posts after fill in the required fields.
One of the main functionalities is subscription which is happening via
Bitcoin payment. I integrate BTCPay server which allows me to handle the payments easily. When the user
clicks on the pay button an invoise appears where the user have to scan the QR code and accept the
payment on his phone. After that the user is subscribed for two months.
I integrated Google-maps so the user can easily see where the apartment or the house is located. Also
to see what conveniences the region have(transport, sport, social life, shops and other). I also integrated
sending emails via Mailgun. For example after successful registration or after subscription  the user recieve
email for that (i can send email only to the email that i have registered in Mailgun at the moment. If i want to
make it work with real user emails i have to pay for upgrade at Mailgun(nothing free in this world..)),
Users can
add filters and search apartments or houses by setting parameters. The main idea
of the project is in the future people to be able not only to subscribe with Bitcoin but also
to pay their rent easily with Bitcoin.
