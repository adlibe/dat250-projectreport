module ListAid exposing (..)

-- Base Haskell function missing from elm
zip : List a -> List b -> List (a, b)
zip = List.map2 (\a b -> (a,b))

-- Removes item at given index
-- If the index is outside of range
-- it will return the list as is
dropAtIndex : Int -> List a -> List a
dropAtIndex index list =
    list
        |> List.indexedMap (\i item -> (i, item))
        |> List.filter (\(i, _) -> i /= index)
        |> List.map Tuple.second 