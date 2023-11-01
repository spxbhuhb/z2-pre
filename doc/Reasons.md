# Reasons

Reasons and decisions made.

## UUID as identifiers

Cons:

* long
  * uses more space
  * hard to read

Pros:

* unique across systems
* prevents structural linking errors
  * with int and long you can put a value into an SQL field that is valid by the constraint but points to the wrong record