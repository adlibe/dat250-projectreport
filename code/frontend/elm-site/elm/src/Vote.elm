module Vote exposing (..)

import Json.Encode as Encode

type alias Vote 
    = {id : Int}

encodeVote : Vote -> Encode.Value
encodeVote vote =
    Encode.object
        [ ( "id", Encode.int vote.id)
        ]

