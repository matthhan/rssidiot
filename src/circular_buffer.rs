extern crate serde;
use serde::ser::{Serialize, Serializer, SerializeSeq};

#[derive(Debug)]
pub struct CircularBuffer<T> where T:Clone {
    
    last_inserted: usize,
    backing_structure:Vec<T>,
    size:usize,
}
impl<T> CircularBuffer<T>  where T:Clone {
    pub fn new(size:usize) -> CircularBuffer<T> {
        CircularBuffer {
            size,
            last_inserted: size - 1,
            backing_structure: Vec::new(),
        }
    }
    pub fn add(self,thing: T) -> CircularBuffer<T> {
        let last_inserted_new = (self.last_inserted + 1) % self.size;
        let backing_structure_new = CircularBuffer::place_in_vec(self.backing_structure,last_inserted_new,thing);
        CircularBuffer {
            size: self.size,
            last_inserted: last_inserted_new,
            backing_structure: backing_structure_new,
        }
    }
    fn place_in_vec(vec:Vec<T>,ind:usize,thing:T) -> Vec<T> {
       let mut newvec = vec.to_vec(); 
        if ind == newvec.len() {
            newvec.push(thing)
        } else {
            newvec[ind] = thing
        }
        newvec
    }
    fn len(&self) -> usize {
        self.backing_structure.len()
    }
}
impl<T> IntoIterator for CircularBuffer<T> where T:Clone {
    type Item = T;
    type IntoIter = CircularBufferIterator<T>;
    fn into_iter(self) -> Self::IntoIter {
        let li = (self.last_inserted + 1) % self.size;
        CircularBufferIterator::new(self,li)
    }
}
pub struct CircularBufferIterator<T> where T:Clone {
    buf: CircularBuffer<T>,
    i: usize,
    done_iterating: bool,
}
impl<T> CircularBufferIterator<T> where T:Clone {
    fn new(buf:CircularBuffer<T>,i:usize) -> CircularBufferIterator<T> {
        CircularBufferIterator {
            i,
            buf,
            done_iterating: false,
        }
    }
}
impl<T> Iterator for CircularBufferIterator<T> where T:Clone {
    type Item = T;
    fn next(&mut self) -> Option<T> {
        if !self.done_iterating && self.buf.backing_structure.len() > 0 {
            let res = Some(self.buf.backing_structure[self.i].clone());
            self.done_iterating = self.i == self.buf.last_inserted;
            self.i = (self.i + 1) % self.buf.size;
            res
        } else {
            None
        }
    }
}
/*impl<T> serde::Serialize for CircularBuffer<T> where T: Clone + serde::Serialize {
    fn serialize<S>(&self, serializer:S) -> Result<S::Ok,S::Error> where S: serde::Serializer {
        let mut seq = serializer.serialize_seq(Some(self.len()))?;
        for elem in self.to_iter() {
            seq.serialize_element(elem)?;
        }
        seq.end()
    }
}*/

#[cfg(test)]
mod circular_buffer_tests {
    use circular_buffer::CircularBuffer;
    extern crate serde_json;
    #[test]
    fn can_create() {
        let buf:CircularBuffer<i32> = CircularBuffer::new(100);
        assert!(true)
    }
    #[test]
    fn can_iterate() {
        let buf:CircularBuffer<i32> = CircularBuffer::new(100);
        for i in buf {
            assert!(true)
        }
    }
    #[test]
    fn can_add() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(30);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        assert!(buf.len() > 0)
    }
    #[test]
    fn iterates_from_oldest() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(10);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        let mut it = buf.into_iter();
        for i in 95..105 {
            match it.next() {
                Some(v) => assert_eq!(v, i),
                None => assert!(false)
            }
        }
        
    }
    /*#[test]
    fn can_be_serialized_and_deserialized_correctly() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(10);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        let asStr = serde_json::to_string(&buf).unwrap();
        println!("{}",asStr);
    }*/
}
