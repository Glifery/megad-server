const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');
const HeaderForm = require('./components/HeaderForm');
const EventList = require('./components/EventList');


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

        client({method: 'GET', path: '/api/events', params: params})
            .done(response => {
                response.entity.forEach(event => {
                    this.state.events.unshift(event);
                })

                this.setState({
                    events: this.state.events,
                    latestEventDate: response.entity.length ? response.entity[response.entity.length - 1].create_date_time : this.state.latestEventDate
                });
            });
    }

    componentDidMount() {
        setInterval(this.updateEvents.bind(this), 2000);
    }

    render() {
        return (
            <>
                <HeaderForm events={1}/>
                <br/>
                <div className="container">
                    <div className="row">
                        <div className="col-sm">
                            <button className="btn btn-primary data-table-clear" onClick={() => {
                                this.setState({
                                    events: [],
                                    latestEventDate: this.state.latestEventDate
                                });
                            }}>Очистить</button>
                        </div>
                    </div>
                </div>
                <br/>
                <EventList events={this.state.events}/>
            </>
        );
    }
}

ReactDOM.render(
    <App />,
    document.getElementById('react')
)