const React = require('react');
const Event = require('./Event');
const client = require('../client');

class EventList extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            events: [],
            latestEventDate: null
        };
    }

    componentDidMount() {
        setInterval(this.updateEvents.bind(this), 2000);
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

    render() {
        const events = this.state.events.map(event =>
            <Event key={event.uuid} event={event}/>
        );
        return (
            <div className="container-fluid">
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
                <table className="table table-sm table-bordered">
                    <thead>
                        <tr>
                            <th>Время</th>
                            <th>Название</th>
                        </tr>
                    </thead>
                    <tbody className="data-table">
                        {events}
                    </tbody>
                </table>
            </div>
        )
    }
}

module.exports = EventList