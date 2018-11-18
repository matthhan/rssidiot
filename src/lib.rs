mod article;
mod feed;
mod circular_buffer;
mod application;
mod cursor;
mod can_replace_at_index;
mod render_state;
mod keymapping;

use keymapping::get_action_for;
use render_state::render_state;
use application::ApplicationState;
use std::io;
#[macro_use]
extern crate serde_derive;
extern crate serde;
extern crate serde_json;

pub fn run() {
    let mut st = ApplicationState::load_from_file("src/example_state.json".to_string());
    let stin = io::stdin();
    let mut input = String::new();

    loop {
        match stin.read_line(&mut input) {
            Ok(_) => {
                st = get_action_for(input.chars().nth(0).unwrap())(st);
                println!("{}",render_state(&st));
                input = "".to_string()
            }
            Err(_) => println!("try again")
        }
    }
}
