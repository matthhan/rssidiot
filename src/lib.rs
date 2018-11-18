mod article;
mod feed;
mod circular_buffer;
mod application;
mod cursor;
mod can_replace_at_index;
mod render_state;
mod example_state;
mod keymapping;

use example_state::construct_example_state;
use keymapping::get_action_for;
use render_state::render_state;
use application::ApplicationState;
use std::io;
#[macro_use]
extern crate serde_derive;
extern crate serde;
extern crate serde_json;

pub fn run() {
    //let mut st = construct_example_state();
    let mut st = ApplicationState::load_from_file("example_state.json".to_string());
    let stin = io::stdin();
    let mut input = String::new();

    //let art = Article::new(String::from("Hello"), String::from("World"));
    //println!("{:?}",art);
    //let art = art.set_read();
    //println!("{:?}",art);
    //println!("{}", serde_json::to_string(&st.feeds).unwrap());
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
