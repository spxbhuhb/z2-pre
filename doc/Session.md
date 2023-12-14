# Sessions

* The URL `/z2/session` sets the `Z2_SESSION` cookie. This identifies the web browser session.
* The browser application requests `/z2/session` before doing anything else.
* When the user logs in `SessionImpl` puts the user session into:
  * `SessionImpl.activeSessions`
  * the service context
* When the user logs out `SessionImpl:
  * removes the user session from `SessionImpl.activeSessions`
  * adds a flag to the service context, so the WebSocker connection will be closed