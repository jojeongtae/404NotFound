import {useParams} from "react-router-dom";

export default function ReportMy() {
    const {username} = useParams();

    // 백엔드 작업중(나의 신고 리스트)

    return (
        <div className="report-my">
            <h3>나의 신고내역</h3>
            <ul className="report-list">

            </ul>
        </div>
    );
}