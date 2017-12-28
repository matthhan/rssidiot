{-# LANGUAGE DeriveGeneric #-}
{-# LANGUAGE OverloadedStrings #-}

module State (State(..)) where

import Data.Aeson
import GHC.Generics
import Feed

data State = State { feeds :: [Feed] } deriving (Show, Generic, Eq)

instance ToJSON State 

instance FromJSON State 
