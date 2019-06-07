import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class RoomList extends Component {

  constructor(props) {
    super(props);
    this.state = {rooms: [], isLoading: true};
    this.remove = this.remove.bind(this);
  }

  componentDidMount() {
    this.setState({isLoading: true});

    fetch('http://localhost:8080/rooms')
      .then(response => response.json())
      .then(data => this.setState({rooms: data, isLoading: false}));
  }

  async remove(id) {
    await fetch(`http://localhost:8080/rooms/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      let updatedRooms = [...this.state.rooms].filter(i => i.id !== id);
      this.setState({rooms: updatedRooms});
    });
  }

  render() {
    const {rooms, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const roomsList = rooms.map(room => {
      return <tr key={room.id}>
        <td style={{whiteSpace: 'nowrap'}}>{room.name}</td>
        <td>{room.location}</td>
        <td>{room.seats}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="danger" onClick={() => this.remove(room.id)}>Delete</Button>
          </ButtonGroup>
        </td>
      </tr>
    });

    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/rooms/new">Create room</Button>
          </div>
          <h3>Rooms</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Name</th>
              <th width="20%">Location</th>
              <th>Seats</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {roomsList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}

export default RoomList;