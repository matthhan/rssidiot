mod article;
mod feed;
mod circular_buffer;
mod application;
mod cursor;
mod vector_extension;
use article::Article;
#[macro_use]
extern crate serde_derive;
extern crate serde;
extern crate serde_json;

pub fn run() {
    let art = Article::new(String::from("Hello"), String::from("World"));
    println!("{:?}",art);
    let art = art.set_read();
    println!("{:?}",art);
}
