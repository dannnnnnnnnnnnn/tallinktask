import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link, withRouter } from 'react-router-dom';

class ConferenceParticipantList extends Component {

  constructor(props) {
    super(props);
    this.state = {participants: [], availableParticipants: [], isLoading: true};
    this.remove = this.remove.bind(this);
    this.add = this.add.bind(this);
  }

  async componentDidMount(id) {
    this.setState({isLoading: true});
    let conferenceParticipants = await (await fetch(`http://localhost:8080/conferences/${this.props.match.params.id}/participants`)).json();
    let allParticipants = await (await fetch(`http://localhost:8080/participants`)).json();
    allParticipants = allParticipants.filter(({id: id1}) => !conferenceParticipants.some(({id: id2}) => id1===id2));
    this.setState({participants:conferenceParticipants, availableParticipants: allParticipants});
    this.setState({isLoading:false})
  }

  async remove(id) {
    await fetch(`http://localhost:8080/conferences/${this.props.match.params.id}/participants/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      let participant = this.state.participants.find(participant => participant.id === id);
      let updatedParticipants = [...this.state.participants].filter(i => i.id !== id);
      this.state.availableParticipants.push(participant);
      this.setState({participants: updatedParticipants});
    });
  }
  async add(id) {
    await fetch(`http://localhost:8080/conferences/${this.props.match.params.id}/participants/${id}`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(()=> {
      let participant = this.state.availableParticipants.find(participant => participant.id === id);
      let updatedAvailableParticipants = this.state.availableParticipants.filter(i => i.id !== id);
      this.state.participants.push(participant);
      this.setState({ availableParticipants: updatedAvailableParticipants});
    })
  }

  render() {
    const {participants, availableParticipants, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    let participantsList = participants.map(participant => {
      return <tr key={participant.id}>
        <td style={{whiteSpace: 'nowrap'}}>{participant.name}</td>
        <td>{participant.birthDate}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="danger" onClick={() => this.remove(participant.id)}>Delete</Button>
          </ButtonGroup>
        </td>
      </tr>
    });
    let availableParticipantsList = availableParticipants.map(participant => {
      return <tr key={participant.id}>
        <td style={{whiteSpace: 'nowrap'}}>{participant.name}</td>
        <td>{participant.birthDate}</td>
        <td>
          <ButtonGroup>
            <Button size="sm" color="info" onClick={() => this.add(participant.id)}>Add</Button>
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
          <h3>Current Participants</h3>
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
            <h3>Add another participant</h3>

          </Table>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="50%">Name</th>
              <th width="50%">Birthdate</th>
            </tr>
            </thead>
            <tbody>
            {availableParticipantsList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }

}

export default withRouter(ConferenceParticipantList);