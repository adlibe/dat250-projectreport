port module Login exposing (..)

import Browser
import Browser.Navigation as Navigation
import Url.Builder as Url

import Html exposing (Html, button, div, text)
import Html.Events exposing (onClick, onInput)
import Html exposing (..)
import Html.Attributes exposing (type_,placeholder,value,for)

import Http exposing (request,jsonBody)
import Json.Decode as D
import Json.Decode exposing (Decoder)
import Json.Encode as Encode

import Poll exposing (..)
import CmdAid exposing (..)

import HostUrl exposing (..)
 

-- MAIN

main =
  Browser.element
    { init = init
    , update = update
    , subscriptions = subscriptions
    , view = view
    }


-- PORTS

port requestSession : () -> Cmd msg
port sendSession : String -> Cmd msg
port recciveSession : (String -> msg) -> Sub msg


-- MODEL

type alias Model 
    = {state : LoginState, username : String, password : String, checkbox : Bool, comment : String}

type LoginState 
    = StateLogin
    | StateRegister

type alias User
    = {username : String, password : String}

user : String -> String -> User
user usr pw = {username = usr, password = pw}

type Msg
  = Login 
  | LoginResponse (Result Http.Error String)
  | GotSession String
  | UpdateUsername String
  | UpdatePassword String
  | Register
  | RegisterResponse (Result Http.Error String)
  | StateSet LoginState

-- HTTP

--- TODO --- DECODERS ARE FRAMEWORK DEPENDANT
loginRequest : User -> Cmd Msg
loginRequest  u = 
        Http.post  
        { url = targetUrlPath "/login",
          body = jsonBody (userToJson u),
          expect = Http.expectJson LoginResponse (D.field "session_id" D.string)
        } 

registerRequest : User -> Cmd Msg
registerRequest u = 
        Http.post  
        { url = targetUrlPath "/register",
          body = jsonBody (userToJson u),
          expect = Http.expectJson RegisterResponse (D.field "message" D.string)
        } 

-- DECODE | ENCODE

userDecoder : Decoder User
userDecoder = D.map2 User
    (D.field "username" D.string)
    (D.field "password" D.string)


userToJson : User -> Encode.Value
userToJson u = 
        Encode.object 
            [  ("username", Encode.string u.username),
               ("password", Encode.string u.password)
            ]



-- HTML

title : LoginState -> Html Msg
title state =
    case state of
        StateLogin ->
            Html.h2 [] [text "Login"]
        StateRegister ->
            Html.h2 [] [text "Register"]

toggleButton : LoginState -> Html Msg
toggleButton state = 
    case state of 
        StateLogin ->
            button
                    [ type_ "button", onClick (StateSet StateRegister) ]
                    [ text "Register Instead" ]
        StateRegister ->
            button
                    [ type_ "button", onClick (StateSet StateLogin) ]
                    [ text "Login Instead" ]

submitButton : LoginState -> Html Msg
submitButton state =
    case state of
        StateLogin ->
            button
                    [ type_ "button", onClick Login ]
                    [ text "Login" ]
        StateRegister ->
            button
                    [ type_ "button", onClick Register ]
                    [ text "Register" ]


-- MAIN

init : () -> (Model, Cmd Msg)
init _ = {state = StateLogin, username = "", password = "", checkbox = False, comment = ""}
            |> withCmd (requestSession ())

update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
  case msg of
    LoginResponse res -> 
        case res of
            Ok id ->
                model  
                    |> withCmd (Cmd.batch [sendSession id, Navigation.load (Url.relative ["Index.html"] [])])
            Err _ ->
                {model | comment = "Login Error"} 
                    |> noCmd
 
    GotSession str -> 
        model |> noCmd

    UpdateUsername usr ->
        {model | username = usr}
            |> noCmd

    UpdatePassword pwd ->
        {model | password = pwd}
            |> noCmd

    Login ->
        model 
            |> withCmd (loginRequest (user model.username model.password))
    
    Register ->
        if ((String.length model.password) >= 5) 
            then 
                model 
                    |> withCmd (registerRequest (user model.username model.password))
            else 
                {model | comment = "Password must be at least 5 characters"}
                    |> noCmd

    RegisterResponse res ->
        case res of 
            Ok str ->
                {state = StateLogin, username = "", password = "", checkbox = False, comment = str}
                    |> noCmd
            Err _ ->
                {model | comment = "Unknown Registration error : Maybe try another username "}
                    |> noCmd

    StateSet s ->
        {model | state = s}
            |> noCmd




subscriptions : Model -> Sub Msg
subscriptions _ =
    recciveSession GotSession

view : Model -> Html Msg
view model =
    div []
            [ 
            title model.state, 
            div [] [text model.comment],

            div []
                [ label [ for "username" ] [ text "Username:" ]
                , input 
                    [ type_ "text",
                    placeholder "Enter your username",
                    value model.username,
                    onInput UpdateUsername
                    ]
                    []
                ],
            div []
                [ label [ for "password" ] [ text "Password:" ]
                , input
                    [ type_ "password",
                    placeholder "Enter your password",
                    value model.password,
                    onInput UpdatePassword
                    ]
                    []
                ],
            submitButton model.state,
            toggleButton model.state
                
            ]
