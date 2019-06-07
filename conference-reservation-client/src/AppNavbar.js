import React, { Component } from 'react';
import { Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem } from 'reactstrap';
import { Link } from 'react-router-dom';

export default class AppNavbar extends Component {
  constructor(props) {
    super(props);
    this.state = {isOpen: false};
    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  render() {
    return <Navbar color="dark" dark expand="md">
      <NavbarBrand tag={Link} to="/">Home</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>
      <NavbarBrand tag={Link} to="/rooms">Rooms</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>
      <NavbarBrand tag={Link} to="/participants">Participants</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>
      <NavbarBrand tag={Link} to="/conferences">Conferences</NavbarBrand>
      <NavbarToggler onClick={this.toggle}/>
      <Collapse isOpen={this.state.isOpen} navbar>
        <Nav className="ml-auto" navbar>
        <NavItem>
          <h1>ConferenceApp</h1>
        </NavItem>
        </Nav>
      </Collapse>
    </Navbar>;
  }
}