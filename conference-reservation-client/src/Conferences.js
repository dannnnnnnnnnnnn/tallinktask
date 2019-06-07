import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class ConferencesList extends Component {

  constructor(props) {
    super(props);
    this.state = {conferences: [], isLoading: true};
    this.remove = this.remove.bind(this);
  }

  componentDidMount() {
    this.setState({isLoading: true});

    fetch('http://localhost:8080/conferences')
      .then(response => response.json())
      .then(data => this.setState({conferences: data, isLoading: false}));
  }

  async remove(id) {
    await fetch(`http://localhost:8080/conferences/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    }).then(() => {
      let updatedConferences = [...this.state.conferences].filter(i => i.id !== id);
      this.setState({conferences: updatedConferences});
    });
  }

  render() {
    const {conferences, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const conferencesList = conferences.map(conference => {
       
      return <tr key={conference.id}>
        <td style={{whiteSpace: 'nowrap'}}>{conference.name}</td>
          {!!conference.room ? <td>{conference.room.name + ', ' + conference.room.location}</td> : <td>Room not set</td>}
          {!!conference.room ? <td>{conference.participants.length + ' (Max ' + conference.room.seats + ')'}</td> : <td>{conference.participants.length}</td>}
        <td>{new Date(conference.startTime).toLocaleString('en-GB', this.props.formatOptions)}</td>
        <td>{new Date(conference.endTime).toLocaleString('en-GB', this.props.formatOptions)}</td>
        
        <td>
          <ButtonGroup>
            <Button size="sm" color="primary" tag={Link} to={"/conferences/" + conference.id}>Edit</Button>
            <Button size="sm" color="info" tag={Link}  to={"/conferences/" + conference.id + "/participants"}>Add participants</Button>
            <Button size="sm" color="danger" onClick={() => this.remove(conference.id)}>Delete</Button>
          </ButtonGroup>
        </td>
      </tr>
    });

    return (
      <div>
        <AppNavbar/>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/conferences/new">Create conference</Button>
          </div>
          <h3>Conferences</h3>
          <Table className="mt-4">
            <thead>
            <tr>
              <th width="20%">Name</th>
              <th width="20%">Location</th>
              <th width="20%">Registered participants</th>
              <th width="20%">Start date</th>
              <th width="20%">End date</th>
              <th width="10%">Actions</th>
            </tr>
            </thead>
            <tbody>
            {conferencesList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}

export default ConferencesList;