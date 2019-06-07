import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label, Table, ButtonGroup } from 'reactstrap';
import AppNavbar from './AppNavbar';
import axios from 'axios';
import "react-datepicker/dist/react-datepicker.css";


class ConferenceEdit extends Component {

  emptyItem = {
    name: '',
    startTime: '',
    endTime: '',
    id: ''
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emptyItem,
      room: '',
      rooms:[],
      error: [],
      isLoading: true
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.addRoom = this.addRoom.bind(this);
    this.validate = this.validate.bind(this);
    this.handleDateStrings = this.handleDateStrings.bind(this);
  }

  async componentDidMount() {
    if (this.props.match.params.id !== 'new') {
      const conference = await (await fetch(`http://localhost:8080/conferences/${this.props.match.params.id}`)).json();
      this.setState({item: conference, isLoading:false});
      let {item} = this.state;
      item.startTime = item.startTime.replace(/T/g , " ");
      item.endTime = item.endTime.replace(/T/g, " ");
      !!conference.room ? this.state.room = conference.room : this.state.room = null  ;
      this.setState({item: item});
    } else {
      this.setState({isLoading: false});
    }
  }

  handleChange(event) {
    const target = event.target;
    let value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }

 validate() {
    const {item} = this.state;
    const regex = /^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1]) (2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]/;
      return item.name.trim().length > 1 && item.name.length < 150 && regex.test(item.startTime) && regex.test(item.endTime)
          && Date.parse(item.startTime) < Date.parse(item.endTime);
    }


  async addRoom(id) {
    const{item, rooms} = this.state;
    let updatedItem = item;
    this.handleDateStrings();
    if (id !== -1) {
      let room = rooms.filter(r => r.id === id);
      updatedItem.room = room[0];
      this.setState({item: updatedItem});
    }
    await fetch('http://localhost:8080/conferences/', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item),
    });
    this.props.history.push('/conferences');
  }
  handleDateStrings() {
    let{item} = this.state;
    item.startTime = item.startTime.replace(" ", "T");
    item.endTime = item.endTime.replace(" ", "T");
    this.setState({item: item});
  }
  async handleSubmit(event) {
    this.setState({isLoading: true});
    event.preventDefault();
    const {item, error} = this.state;
    this.handleDateStrings();
    if (item.room === null) {
      item.room = '';
      this.setState({item: item})
    }
    await axios({url: 'http://localhost:8080/conferences/rooms',
      method: 'post',
      data: item

    }).then( response => response.data)
        .then(data=> this.setState({rooms: data}))
        .catch(err => this.setState({error: err.request}));
    this.setState({isLoading: false});
  };

  render() {
    const {item, isLoading, room, rooms, error} = this.state;
    let roomInfo = '';
    let roomsList = '';
    if (isLoading) {
      return "Loading...";
    }
    const title = <h2>{item.id ? 'Edit conference' : 'Add conference'}</h2>;
    if (error.length === 0) {
      roomsList = rooms.map(room => {
        return <tr key={room.id}>
          <td style={{whiteSpace: 'nowrap'}}>{room.name}</td>
          <td>{room.location}</td>
          <td>{room.seats}</td>
          <td>
            <ButtonGroup>
              <Button size="sm" color="info" onClick={() => this.addRoom(room.id)}>Select room</Button>
            </ButtonGroup>
          </td>
        </tr>
      });
    }
    room !== null && room.hasOwnProperty('name') ? roomInfo = 'Current room ' + item.room.name + ', ' + item.room.location : roomInfo = 'No room selected'
    return <div>
      <AppNavbar/>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
          <FormGroup controlId="formGroupName">
            <Label for="name">Name</Label>
            <Input type="text" name="name" id="name" value={item.name || ''}
                   onChange={this.handleChange} autoComplete="name" errorText="Yep!"/>
          </FormGroup>
          <FormGroup>
            <Label for="startTime">Start time (format YYYY-MM-DD HH:MM:SS)</Label>
            <Input type="text" name="startTime" id="startTime" value={item.startTime || ''}
                   maxLength="19" onChange={this.handleChange} autoComplete="startTime"/>
          </FormGroup>
          <FormGroup>
            <Label for="endTime">End time (format YYYY-MM-DD HH:MM:SS)</Label>
            <Input type="text" name="endTime" id="endTime" value={item.endTime || ''}
                   maxLength="19" onChange={this.handleChange} autoComplete="endTime"/>
          </FormGroup>
          <h5 color="primary">{roomInfo}</h5>
          <FormGroup>
            <Button color="primary" type="submit" disabled={!this.validate()}>Get rooms</Button>{' '}
            <Button color="primary" hidden={this.props.match.params.id !== 'new'} onClick={() => this.addRoom(-1)} disabled={!this.validate()}>Create conference without assigning room</Button>{' '}
            <Button color="secondary" tag={Link} to="/conferences">Cancel</Button>
          </FormGroup>
        </Form>
        <h3>Available Rooms</h3>
        <Table className="mt-4">
          <thead>
          <tr>
            <th width="20%">Name</th>
            <th width="20%">Location</th>
            <th width="20%">Available seats</th>
            <th width="20%">Action</th>
          </tr>
          </thead>
          <tbody>
          {roomsList}
          </tbody>
        </Table>
      </Container>
    </div>
  }

}

export default withRouter(ConferenceEdit);