/* Uncomment this to finalize the code

/*
  Add necessary definitions where indicated to make the statement in the main work as specified there.
  Do not add any new classes / traits
*/

package h2

import scala.collection.mutable


class Session

trait SessionProvider:
  def session: Session


trait DefaultSessionProvider extends SessionProvider:
  val dummySession = new Session
  override def session = dummySession


abstract class Identity

trait IdentityCache:
  /* ... Add whatever needed, but no definitions of new methods ...*/

  def getOrAuthenticate(): /* ... Add type, but no function body ... */


trait InMemoryIdentityCache extends IdentityCache:
  /* ... Add whatever needed, but no definitions of methods ...*/

  val cache = mutable.Map.empty[Session, /* ... */]

  override def getOrAuthenticate(): /* ... */ =
    cache.getOrElseUpdate(session, authenticate())


trait Authenticator:
  /* ... Add whatever needed, but no definitions of new methods ...*/

  def authenticate(): /* ... Add type, but no function body ... */


trait UsesSAMLIdentity:
  /* ... Add whatever needed, but no definitions of new methods ...*/


class SAMLIdentity(val saml: String) extends Identity

trait SAMLAuthenticator extends Authenticator with UsesSAMLIdentity:
  val dummySAMLIdentity = new SAMLIdentity("XXX")

  override def authenticate() = dummySAMLIdentity


trait RoleManager:
  def hasRole(role: String): Boolean


trait SAMLRoleManager extends RoleManager with UsesSAMLIdentity:
  /* ... Add whatever needed, but no definitions of new methods ...*/

  override def hasRole(role: String): Boolean =
    val identity = getOrAuthenticate()
    identity.saml == "XXX"


object WebApp extends /* ... Extend traits as per the cake pattern ... */:
  def main(args: Array[String]): Unit =
    println(hasRole("YYY")) // Prints "true"

*/
