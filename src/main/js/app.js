const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');


class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            events: [],
            latestEventDate: null
        };
    }

    updateEvents() {
        let params = this.state.latestEventDate ? {start_date: this.state.latestEventDate} : null;

        client({method: 'GET', path: '/api/monitoring/events', params: params})
            .done(response => {
                this.setState({
                    events: this.state.events.concat(response.entity),
                    latestEventDate: response.entity.length ? response.entity[response.entity.length - 1].create_date_time : this.state.latestEventDate
                });
            });
    }

    componentDidMount() {
        setInterval(this.updateEvents.bind(this), 2000);
    }

    render() {
        return (
            <EventList events={this.state.events}/>
        );
    }
}

class EventList extends React.Component{
    render() {
        const events = this.props.events.map(event =>
            <Event key={event.uuid} event={event}/>
        );
        return (
            <table>
                <tbody>
                <tr>
                    <th>Время</th>
                    <th>Название</th>
                </tr>
                {events}
                </tbody>
            </table>
        )
    }
}

class Event extends React.Component{
    render() {
        return (
            <tr>
                <td>{this.props.event.event_date_time}</td>
                <td>{this.props.event.name}</td>
            </tr>
        )
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)