module CmdAid exposing (..)

noCmd : a -> (a,Cmd msg)
noCmd model = (model,Cmd.none)

withCmd : Cmd msg -> a  -> (a,Cmd msg)
withCmd cmd model = (model,cmd)