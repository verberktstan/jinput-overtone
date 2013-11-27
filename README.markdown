# jinput-overtone

Bringing JInput devices to Overtone, the Clojure music project

[JInput][] is a Java library for working with devices such as gamepads
and joysticks.

__N.B.__ This code is currently in transition to use overtone.libs.event.

## Identifying Your Controllers
You can work with controllers using name strings. To discover these
name strings from attached devices:

    (map find-name (find-controllers))
    
Sometimes the names returned by the devices are vague, for example "USB Game Controllers" is the name
of one joystick I have tested. They may be more useful, such as "Controller (XBOX 360 For Windows)".

The utility function (find-controller x) does a regular expression match using its string
parameter, so you can specify just enough to uniquely identify the specific controller
you want to work with e.g.

    (find-controller "XBOX")

Tip: Plug in the controllers you want to work with before starting a REPL.

## Usage

Start the event loop with

    (def t (start-input))
    
t is the Thread, you can stop it with

    (stop-input t)

Attaching functions to events

    (defonce xbox (find-controller "XBOX"))

    (controller-event-handlers
      xbox
      {BUTTON0 #(when (button-pressed? (:val %)) (tune-frere mout 0 :acoustic-grand-piano ))
       BUTTON1 #(when (button-pressed? (:val %)) (tune-frere mout 1 :tubular-bells ))
       BUTTON2 #(when (button-pressed? (:val %)) (tune-frere mout 2 :trumpet ))
       BUTTON3 #(when (button-pressed? (:val %)) (tune-frere mout 3 :electric-guitar-clean ))
       BUTTON4 #(when (button-pressed? (:val %)) (percuss mout :cowbell ))
       BUTTON5 #(when (button-pressed? (:val %)) (percuss mout :open-triangle ))
       BUTTON6 #(when (button-pressed? (:val %)) (percuss mout :maracas ))
       BUTTON7 #(when (button-pressed? (:val %)) (percuss mout :tambourine ))
       X-AXIS #(println "X = "(:val %))
       HAT #(condp hat-direction? (:val %)
              HAT_N (percuss mout :open-hi-hat )
              HAT_W (percuss mout :crash-cymbal-1 )
              HAT_E (percuss mout :crash-cymbal-2 )
              HAT_S (percuss mout :pedal-hi-hat )
              nil)})
              
## Development
This project is being developed and tested with lein 2 and Overtone on Windows.

## License

Copyright (C) 2012-2013 James Petry.

Distributed under the Eclipse Public License, the same as Clojure.

[jinput]: https://java.net/projects/jinput "jinput"

