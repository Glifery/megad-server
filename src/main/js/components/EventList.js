const React = require('react');
const Event = require('./Event');

class EventList extends React.Component{
    render() {
        const events = this.props.events.map(event =>
            <Event key={event.uuid} event={event}/>
        );
        return (
            <div className="container-fluid">
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