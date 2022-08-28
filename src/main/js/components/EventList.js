const React = require('react');
const Event = require('./Event');
const client = require('../client');

class EventList extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            events: [],
            latestEventDate: null,
            progress: 'done'
        };
    }

    componentDidMount() {
        setInterval(this.updateEvents.bind(this), 2000);
    }

    updateEvents() {
        this.setState({progress: 'loading'});

        let params = this.state.latestEventDate ? {start_date: this.state.latestEventDate} : null;

        client({method: 'GET', path: '/api/events', params: params})
            .then(response => {
                response.entity.forEach(event => {
                    this.state.events.unshift(event);
                })

                this.setState({
                    events: this.state.events,
                    latestEventDate: response.entity.length ? response.entity[response.entity.length - 1].create_date_time : this.state.latestEventDate,
                    progress: 'done'
                });
            }).catch(() => {
                this.setState({
                    progress: 'error'
                });
            });
    }

    renderReloadIcon(param) {
        switch(this.state.progress) {
            case 'progress':
                return <i className="fa-solid fa-rotate"></i>;
            case 'done':
                return <i class="fa-solid fa-check"></i>;
            default:
                return <i class="fa-solid fa-triangle-exclamation"></i>;
        }
    }

    render() {
        const events = this.state.events.map(event =>
            <Event key={event.uuid} event={event}/>
        );
        return (
            <div className="container-fluid">
                <div className="row">
                    <div className="col-sm">
                        <div className="btn-group" role="group">
                            <button className="btn btn-danger" onClick={() => {
                                this.setState({
                                    events: [],
                                    latestEventDate: this.state.latestEventDate
                                });
                            }}>Clear</button>
                            <button type="button" className="btn btn-primary" disabled>
                                Reloading {this.renderReloadIcon()}
                            </button>
                        </div>
                    </div>
                </div>
                <br/>
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