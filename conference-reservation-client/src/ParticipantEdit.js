import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';


class ParticipantEdit extends Component {

  emptyItem = {
    name: '',
    birthDate: '',
    id: ''
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emptyItem
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.validate = this.validate.bind(this);
  }

  async componentDidMount() {
    if (this.props.match.params.id !== 'new') {
      const participant = await (await fetch(`http://localhost:8080/participants/${this.props.match.params.id}`)).json();
      this.setState({item: participant});
    }
  }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }
  validate() {
      return this.state.item.name.trim().length > 0 
          && /^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])/.test(this.state.item.birthDate)
          && Date.parse(this.state.item.birthDate) < Date.now();
  }
  async handleSubmit(event) {
    event.preventDefault();
    const {item} = this.state;

    await fetch('http://localhost:8080/participants/', {
      method: (item.id) ? 'PUT' : 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
     
      body: JSON.stringify(item),
    });
    this.props.history.push('/participants');
  }

  render() {
    const {item} = this.state;
    const title = <h2>{item.id ? 'Edit participant' : 'Add participant'}</h2>;

    return <div>
      <AppNavbar/>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
          <FormGroup>
            <Label for="name">Name</Label>
            <Input type="text" name="name" id="name" value={item.name || ''}
                   onChange={this.handleChange} autoComplete="name"/>
          </FormGroup>
          <FormGroup>
            <Label for="birthDate">Birthdate (Format: year-month-day)</Label>
            <Input type="text" name="birthDate" id="birthDate" value={item.birthDate || ''}
                   onChange={this.handleChange} autoComplete="birthDate" maxLength={10}/>
          </FormGroup>
          <FormGroup>
            <Button color="primary" type="submit" disabled={!this.validate()}>Save</Button>{' '}
            <Button color="secondary" tag={Link} to="/participants">Cancel</Button>
          </FormGroup>
        </Form>
      </Container>
    </div>
  }
}

export default withRouter(ParticipantEdit);