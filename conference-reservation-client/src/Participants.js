import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class ParticipantsList extends Component {

  constructor(props) {
    super(props);
    this.state = {participants: [], isLoading: true};
    this.remove = this.remove.bind(this);
  }

  componentDidMount() {
    this.setState({isLoading: true});

    fetch('http://localhost:8080/participants')
      .then(response => response.json())
      .then(data => this.setState({participants: data, isLoading: false}));
  }

  async remove(id) {
    await fetch(`http://localhost:8080/participants/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      const updatedParticipants = [...this.state.participants].filter(i => i.id !== id);
      this.setState({participants: updatedParticipants});
    });
  }

  render() {
    const {participants, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const participantsList = participants.map(participant => {
      return <tr key={participant.id}>
        <td style={{whiteSpace: 'nowrap'}}>{participant.name}</td>
        <td>{participant.birthDate}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/participants/" + participant.id}>Edit</Button>
            <Button size="sm" color="danger" onClick={() => this.remove(participant.id)}>Delete</Button>
          </ButtonGroup>
        </td>
      </tr>
    });

    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/participants/new">Create participant</Button>
          </div>
          <h3>Participants</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="50%">Name</th>
              <th width="50%">Birthdate</th>
            </tr>
            </thead>
            <tbody>
            {participantsList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}

export default ParticipantsList;