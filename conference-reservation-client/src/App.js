import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import RoomEdit from './RoomEdit'
import RoomList from './Rooms';
import ConferenceEdit from './ConferenceEdit'
import ConferencesList from './Conferences';
import ParticipantsList from './Participants';
import ParticipantEdit from './ParticipantEdit';
import ConferenceParticipantList from './ConferenceParticipants'

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path='/' exact={true} component={Home}/>
          <Route path='/rooms' exact={true} component={RoomList}/>
          <Route path='/rooms/:id' exact={true} component={RoomEdit}/>
          <Route path='/conferences/:id' exact={true} component={ConferenceEdit}/>
          <Route path='/conferences' exact={true} component={ConferencesList}/>
          <Route path='/participants' exact={true} component={ParticipantsList}/>
          <Route path='/participants/:id' exact={true} component={ParticipantEdit}/>
          <Route path='/conferences/:id/participants' exact={true} component={ConferenceParticipantList}/>

        </Switch>
      </Router>
    )
  }
}

export default App;